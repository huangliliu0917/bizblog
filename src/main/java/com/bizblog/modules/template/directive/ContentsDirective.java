/**
 *
 */
package com.bizblog.modules.template.directive;

import com.bizblog.base.lang.Consts;
import com.bizblog.base.utils.BeanMapUtils;
import com.bizblog.modules.data.PostVO;
import com.bizblog.modules.entity.Channel;
import com.bizblog.modules.service.ChannelService;
import com.bizblog.modules.service.PostService;
import com.bizblog.modules.template.DirectiveHandler;
import com.bizblog.modules.template.TemplateDirective;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 文章内容查询
 * <p>
 * 示例：
 * 请求：http://mtons.com/index?order=newest&pn=2
 * 使用：@contents group=x pn=pn order=order
 *
 * @author langhsu
 */
@Component
public class ContentsDirective extends TemplateDirective {
    @Autowired
    private PostService postService;
    @Autowired
    private ChannelService channelService;

    @Override
    public String getName() {
        return "contents";
    }

    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        Integer channelId = handler.getInteger("channelId", 0);
        String order = handler.getString("order", Consts.order.NEWEST);

        Set<Integer> excludeChannelIds = new HashSet<>();

        if (channelId <= 0) {
            List<Channel> channels = channelService.findAll(Consts.STATUS_CLOSED);
            if (channels != null) {
                channels.forEach((c) -> excludeChannelIds.add(c.getId()));
            }
        }

        Pageable pageable = wrapPageable(handler, Sort.by(Sort.Direction.DESC, BeanMapUtils.postOrder(order)));
        Page<PostVO> result = postService.paging(pageable, channelId, excludeChannelIds);
        handler.put(RESULTS, result).render();
    }
}
