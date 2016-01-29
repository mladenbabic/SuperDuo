package barqsoft.footballscores.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import barqsoft.footballscores.R;
import barqsoft.footballscores.api.CursorRecyclerViewAdapter;
import barqsoft.footballscores.api.FixtureModel;
import barqsoft.footballscores.util.Status;
import barqsoft.footballscores.util.Utilities;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class ScoresAdapter extends CursorRecyclerViewAdapter<ScoresAdapter.ViewHolder> {

    private final Context mContext;
    private String FOOTBALL_SCORES_HASHTAG = "#Football_Scores";
    private static final int TYPE_CELL = 1;

    @Override
    public int getItemViewType(int position) {
        return TYPE_CELL;
    }

    public ScoresAdapter(Context context, Cursor c) {
        super(context, c);
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scores_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, Cursor cursor) {
        FixtureModel bookModel = new FixtureModel().fromCursor(cursor);
        viewHolder.homeNameView.setText(bookModel.getHomeName());
        viewHolder.awayNameView.setText(bookModel.getAwayHome());
        viewHolder.dateView.setText(bookModel.getDate());

        if (bookModel.getScoreHome() != -1 && bookModel.getScoreAway() != -1) {
            viewHolder.scoreHomeView.setText("" + bookModel.getScoreHome());
            viewHolder.scoreAwayView.setText("" + bookModel.getScoreAway());

            if (Status.FINISHED.equals(bookModel.getMatchStatus())) {
                viewHolder.mMatchStatusView.setText(mContext.getString(R.string.match_detail_finished));
            } else {
                viewHolder.mMatchStatusView.setText(mContext.getString(R.string.match_detail_timed));
            }
        } else {
            viewHolder.scoreHomeView.setText("0");
            viewHolder.scoreAwayView.setText("0");
        }

        viewHolder.mMatchdayTextView.setText(Utilities.getMatchDay(mContext, bookModel.getMatchday(), bookModel.getSessionId()));
        viewHolder.mLeagueTextView.setText(Utilities.getLeague(mContext, bookModel.getSessionId()));

        viewHolder.mShareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(createShareIntent(viewHolder.homeNameView.getText() + " " + viewHolder.scoreHomeView.getText() + " - " +
                                        viewHolder.scoreAwayView.getText() + " " + viewHolder.awayNameView.getText() + " "
                        )
                );
            }
        });

        viewHolder.mShareImageView.setContentDescription(mContext.getString(R.string.match_detail_share_match));

        Glide.with(mContext)
                .load(Utilities.getTeamCrestURL(bookModel.getCrestHomeUrl()))
                .error(R.drawable.ic_launcher)
                .placeholder(R.drawable.ic_launcher)
                .into(viewHolder.homeCrestView);

        Glide.with(mContext)
                .load(Utilities.getTeamCrestURL(bookModel.getCrestAwayUrl()))
                .error(R.drawable.ic_launcher)
                .placeholder(R.drawable.ic_launcher)
                .into(viewHolder.awayCrestView);

        viewHolder.homeCrestView.setContentDescription(bookModel.getHomeName());
        viewHolder.awayCrestView.setContentDescription(bookModel.getAwayHome());

    }

    public Intent createShareIntent(String value) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, value + FOOTBALL_SCORES_HASHTAG);
        return shareIntent;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.home_name)
        TextView homeNameView;
        @Bind(R.id.away_name)
        TextView awayNameView;
        @Bind(R.id.score_home_textview)
        TextView scoreHomeView;
        @Bind(R.id.score_away_textview)
        TextView scoreAwayView;
        @Bind(R.id.data_textview)
        TextView dateView;
        @Bind(R.id.match_status)
        TextView mMatchStatusView;
        @Bind(R.id.detail_textview_league)
        TextView mLeagueTextView;
        @Bind(R.id.detail_textview_matchday)
        TextView mMatchdayTextView;
        @Bind(R.id.home_crest)
        ImageView homeCrestView;
        @Bind(R.id.away_crest)
        ImageView awayCrestView;
        @Bind(R.id.detail_button_share)
        ImageView mShareImageView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
